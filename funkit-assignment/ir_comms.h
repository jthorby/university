/**
  File: ir_comms.h
  Authors: Justin Thorby, Matt Hodgett
  Date: 12 Oct 2016
  Description: Take care of IR communications between 2 UCFK-4's
*/

#ifndef IR_COMMS_H
#define IR_COMMS_H

#include "system.h"
#include "ir_uart.h"

#define ROCK 7
#define PAPER 50
#define SNIPS 100
#define NO_MOVE 0


/** Reset the opponent's move, so game can be re-played. */
void reset_opponent(void);

/** Send player's move over IR.
    @param player's move to send over IR. */
void send_move(int8_t move);

/** Task to get opponent's move over IR. */
void get_response_task(__unused__ void *data);

/** Getter for opponent's move, for use in other modules.
    @return the value received over IR for the opponent.  */
int8_t get_opponent(void);

#endif