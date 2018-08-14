/**
  File: game.h
  Authors: Justin Thorby, Matt Hodgett
  Date: 12 Oct 2016
  Description: Main file for a Paper-Snips-Rock game
*/

#ifndef GAME_H
#define GAME_H

#include "system.h"
#include "pacer.h"
#include "navswitch.h"
#include "ir_uart.h"
#include "tinygl.h"
#include "../fonts/font5x7_1.h"
#include "pio.h"
#include "ledmat.h"
#include "task.h"
#include "messages.h"
#include "select_move.h"
#include "ir_comms.h"

#define DISPLAY_RATE 250
#define GAME_RATE 50
#define MESSAGE_RATE 15


/** Set up a Paper-Snips-Rock game.
    Initialise necessary components such as task schedule, display navswitch and
    IR communications.
    @return exit status. */
int main(void);

#endif