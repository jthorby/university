/**
  File: select_move.h
  Authors: Justin Thorby, Matt Hodgett
  Date: 12 Oct 2016
  Description: Take care of selecting moves and deciding game outcomes
*/

#ifndef GAME_LOGIC_H
#define GAME_LOGIC_H

#include "system.h"
#include "navswitch.h"
#include "tinygl.h"
#include "../fonts/font5x7_1.h"
#include "messages.h"
#include "ir_uart.h"
#include "ir_comms.h"

#define ROCK 7
#define PAPER 50
#define SNIPS 100
#define NO_MOVE 0


/** Setter for player's move. */
void set_opponent_task(__unused__ void *data);

/** Task to handle setting player's move when navswitch pushed in certain
    directions. Once player's move sent over IR, checks if oppnent's move has
    been received, if not, waits for the opponent's move and then calls
    decide(). */
void select_move_task(__unused__ void *data);

#endif