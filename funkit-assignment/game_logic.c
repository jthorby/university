/**
  File: select_move.c
  Authors: Justin Thorby, Matt Hodgett
  Date: 12 Oct 2016
  Description: Take care of selecting moves and deciding game outcomes for a
               Paper-Snips-Rock game
*/

#include "system.h"
#include "navswitch.h"
#include "messages.h"
#include "ir_uart.h"
#include "ir_comms.h"

#define ROCK 7
#define PAPER 50
#define SNIPS 100
#define NO_MOVE 0


static uint8_t move = NO_MOVE;
static uint8_t opponent = NO_MOVE;

/** Task to set the opponent's move.  */
void set_opponent_task(__unused__ void *data)
{
  opponent = get_opponent();
}

/** Take appropriate action based on player's and opponent's moves by displaying
    a win/loss/draw message.  */
static void decide(void)
{
  if (move == opponent) {
    draw_message();
  }
  else if (move == ROCK) {
    if (opponent == SNIPS) {
      win_message();
    }
    else if (opponent == PAPER) {
      lose_message();
    }
  }
  else if (move == PAPER) {
    if (opponent == ROCK) {
      win_message();
    }
    else if (opponent == SNIPS) {
      lose_message();
    }
  }
  else if (move == SNIPS) {
    if (opponent == PAPER) {
      win_message();
    }
    else if (opponent == ROCK) {
      lose_message();
    }
  }
  opponent = NO_MOVE;
  reset_opponent();
}

/** Task to handle setting player's move when navswitch pushed in certain
    directions. Once player's move sent over IR, checks if oppnent's move has
    been received, if not, waits for the opponent's move and then calls
    decide(). */
void select_move_task(__unused__ void *data)
{
  navswitch_update();

  if (navswitch_push_event_p(NAVSWITCH_NORTH)) {
    rock_message();
    move = ROCK;
  }
  else if (navswitch_push_event_p(NAVSWITCH_EAST)) {
    paper_message();
    move = PAPER;
  }
  else if (navswitch_push_event_p(NAVSWITCH_WEST)) {
    snips_message();
    move = SNIPS;
  }
  else if (navswitch_push_event_p(NAVSWITCH_PUSH)) {
    clear_screen();

    send_move(move);

    /** The first player to select their move will not have any opponent move to
        act on, so wait until a move has been received. Only update the opponent
        if it is a valid move. */
    while ((opponent == NO_MOVE) && (opponent != ROCK) && (opponent != SNIPS) &&
          (opponent != PAPER)) {
      opponent = ir_uart_getc();
    }

    decide();
  }
}