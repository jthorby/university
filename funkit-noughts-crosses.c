#include "system.h"
#include "pacer.h"
#include "navswitch.h"
#include "ir_uart.h"
#include "tinygl.h"
#include "../fonts/font5x7_1.h"
#include "pio.h"
#include "ledmat.h"
#include "task.h"

int8_t current_player = -1; // no current_player at initialisation
int8_t player1 = 1;
int8_t player2 = 2;
uint8_t row = 4;
uint8_t col = 2;
uint8_t game_won = 0;
uint8_t my_turn = 0;

uint8_t board[7][5] =  {
                        {-1, -1, -1, -1, -1}, // these are the empty rows on the bottom of the matrix
                        {-1, -1, -1, -1, -1}, // ""
        				{0, -1, 0, -1, 0},    // 0's = no piece
        				{-1, -1, -1, -1, -1}, // -1's = not slots on the board
        				{0, -1, 0, -1, 0},
                        {-1, -1, -1, -1, -1},
                        {0, -1, 0, -1, 0},
    			 	   };


void clear_board(void) {
 int8_t rows = 6;
 int8_t cols = 4;

 for (; rows >= 2; rows -= 2){
   for (; cols >= 0; cols -= 2) {
     board[rows][cols] = 0;
   }
 }
}

void win(void) {
  game_won = 1;
  tinygl_clear();
  clear_board();
  tinygl_update();
  tinygl_font_set (&font5x7_1);
  tinygl_text_speed_set (10);
  tinygl_text_mode_set (TINYGL_TEXT_MODE_SCROLL);
  tinygl_text ("WINNER ");
}

void check_diags(void) {
  int8_t row_check = 2;
  int8_t col_check = 0;
  int8_t winner = 1;

  while(row_check <= 6) { // check the top left diagonal
    if (!(board[row_check][col_check] == current_player)) {
      winner = 0;
    }
    row_check += 2;
    col_check += 2;
  }

  if (winner == 1) {
    win();
  }

  else { // check the top right diagonal
    winner = 1;
    row_check = 2;
    col_check = 4;
    while (row_check <= 6) {
      if (!(board[row_check][col_check] == current_player)) {
        winner = 0;
      }
      row_check += 2;
      col_check -= 2;
    }

    if (winner == 1) {
      win();
    }
  }
}


void check_col(void) {
  int8_t row_check = 2; // iterate over rows on current column

  int8_t winner = 1;
  for (; row_check <= 6; row_check += 2) {
    if (!(board[row_check][col] == current_player)) {
      winner = 0;
    }
  }

  if (winner == 1) {
    win();
  }
}


void check_row(void) {
  int8_t col_check = 0; // iterate over columns on current row

  int8_t winner = 1;
  for (; col_check <= 4; col_check += 2) {
    if (!(board[row][col_check] == current_player)) {
      winner = 0;
    }
  }

  if (winner == 1) {
    win();
  }
}


void check_for_win(void) {
  check_row();
  check_col();
  check_diags();
  if (current_player == player1) {
    current_player = player2;
  } else {
    current_player = player1;
  }
}


void check_selection(void) {
  if (navswitch_push_event_p(NAVSWITCH_PUSH)) {
    board[row][col] = current_player; //Place current player's piece on board in current position
    check_for_win();
  }
}


void move_selector(tinygl_point_t pixel) {

  if (navswitch_push_event_p(NAVSWITCH_NORTH)) {
    if (row > 2) {
      row -= 2;
      tinygl_pixel_set(pixel, 0);
    }
  }

  if (navswitch_push_event_p(NAVSWITCH_SOUTH)) {
    if (row < 6) {
      row += 2;
      tinygl_pixel_set(pixel, 0);
    }
  }

  else if (navswitch_push_event_p(NAVSWITCH_EAST)) {
    if (col < 4) {
      col += 2;
      tinygl_pixel_set(pixel, 0);
    }
  }

  else if (navswitch_push_event_p(NAVSWITCH_WEST)) {
    if (col > 0) {
      col -= 2;
      tinygl_pixel_set(pixel, 0);
    }
  }

  pixel.x = col;
  pixel.y = row;

  tinygl_pixel_set(pixel,  1);
}


void display_winner_task(__unused__ void *data) {
  if (game_won == 1) {
    while(1) {
      tinygl_update();
      break;
    }
  }
}

void update_display_task(__unused__ void* data) {
    tinygl_update();
}


void selection_task(__unused__ void *data) {
  if (!game_won) {
    navswitch_update();
    tinygl_point_t pixel = {col, row};
    move_selector(pixel);
    check_selection();
  }
}


uint8_t sequence_fast = 0;
void fast_flash_task(__unused__ void *data) { //player 1
  if (!game_won) {
    int8_t rows = 6;
    int8_t cols = 4;
    tinygl_point_t point_to_flash = {cols, rows};

    for (; rows >= 2; rows -= 2) {
      for (; cols >= 0; cols -= 2) {
        if ((board[rows][cols]) == player1) {
          point_to_flash.x = cols;
          point_to_flash.y = rows;
          tinygl_pixel_set(point_to_flash, sequence_fast);
        }
      }
      cols = 4;
    }
    sequence_fast = 1 - sequence_fast;
  }
}

uint8_t sequence_slow = 0;
void slow_flash_task(__unused__ void *data) { //player 2
  if (!game_won) {
    int8_t rows = 6;
    int8_t cols = 4;
    tinygl_point_t point_to_flash = {cols, rows};

    for (; rows >= 2; rows -= 2) {
      for (; cols >= 0; cols -= 2) {
        if ((board[rows][cols]) == player2) {
          point_to_flash.x = cols;
          point_to_flash.y = rows;
          tinygl_pixel_set(point_to_flash, sequence_slow);
        }
      }
      cols = 4;
    }
    sequence_slow = 1 - sequence_slow;
  }
}

void wait_for_input(void) {
  int8_t done = 0;
  while(done == 0) {
    navswitch_update();

    if (navswitch_push_event_p(NAVSWITCH_PUSH)) {
      done = 1;
      current_player = player1;
      my_turn = 1;
    }
  }
}

int main(void) {

    task_t tasks[] =
    {
      {.func = selection_task, .period = TASK_RATE / 250},
      {.func = slow_flash_task, .period = TASK_RATE / 2},
      {.func = fast_flash_task, .period = TASK_RATE / 50},
      {.func = update_display_task, .period = TASK_RATE / 250},
      {.func = display_winner_task, .period = TASK_RATE / 250},
    };

    system_init();
    tinygl_init(250);
    navswitch_init ();
    ir_uart_init();

    wait_for_input();

    task_schedule(tasks, 5);
}
