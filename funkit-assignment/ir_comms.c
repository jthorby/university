/**
  File: ir_comms.c
  Authors: Justin Thorby, Matt Hodgett
  Date: 12 Oct 2016
  Description: Take care of IR communications between 2 UCFK-4's, and allow
               other modules to access the current opponent in a Paper-Snips-Rock
               game
*/

#include "system.h"
#include "ir_uart.h"

#define ROCK 7
#define PAPER 50
#define SNIPS 100
#define NO_MOVE 0


static uint8_t current_opponent = NO_MOVE;

/** Reset the opponent's move, so game can be re-played.  */
void reset_opponent(void)
{
  current_opponent = NO_MOVE;
}

/** Send player's move over IR. */
void send_move(uint8_t move)
{
  ir_uart_putc(move);
}

/** Task to get opponent's move over IR. Only update the current_opponent if it
    is a valid opponent.  */
void get_response_task(__unused__ void *data) // change to listen for opponent task
{
  uint8_t possible_opponent = NO_MOVE;

  if (ir_uart_read_ready_p()) {
    possible_opponent = ir_uart_getc();

    if ((possible_opponent == ROCK) || (possible_opponent == PAPER) ||
        (possible_opponent == SNIPS)) {
          current_opponent = possible_opponent;
    }
  }
  possible_opponent = NO_MOVE;
}

/** Get the opponent's move.  */
int8_t get_opponent(void)
{
  return current_opponent;
}