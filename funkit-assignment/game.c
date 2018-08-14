/**
  File: game.c
  Authors: Justin Thorby, Matt Hodgett
  Date: 12 Oct 2016
  Description: Main file for a Paper-Snips-Rock game
*/

#include "system.h"
#include "navswitch.h"
#include "ir_uart.h"
#include "tinygl.h"
#include "../fonts/font5x7_1.h"
#include "task.h"
#include "messages.h"
#include "game_logic.h"
#include "ir_comms.h"

#define DISPLAY_RATE 250
#define GAME_RATE 50
#define MESSAGE_RATE 15


/** Task to handle updating tinygl. */
static void update_display_task(__unused__ void *data)
{
  tinygl_update();
}

/** Set up a Paper-Snips-Rock game.
    Initialise necessary components such as task schedule, display, navswitch and
    IR communications. */
int main(void)
{
  system_init();
  tinygl_init(DISPLAY_RATE);
  tinygl_font_set(&font5x7_1);
  tinygl_text_speed_set(MESSAGE_RATE);
  navswitch_init ();
  ir_uart_init();
  welcome_message();

  task_t tasks[] =
    {
      {.func = update_display_task, .period = TASK_RATE / DISPLAY_RATE},
      {.func = select_move_task, .period = TASK_RATE / GAME_RATE},
      {.func = get_response_task, .period = TASK_RATE / GAME_RATE},
      {.func = set_opponent_task, .period = TASK_RATE / GAME_RATE},
    };

  task_schedule(tasks, 4);

  return 1;
}