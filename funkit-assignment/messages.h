/**
  File: messages.h
  Authors: Justin Thorby, Matt Hodgett
  Date: 12 Oct 2016
  Description: Display a variety of messages/characters for a Paper-Snips-Rock
               game.
*/

#ifndef MESSAGES_H
#define MESSAGES_H

#include "system.h"
#include "tinygl.h"
#include "../fonts/font5x7_1.h"


/** Display a welcome message. */
void welcome_message(void);

/** Clear the screen to hide player's move. */
void clear_screen(void);

/** Display a "W" to show a win. */
void win_message(void);

/** Display an "L" to show a loss. */
void lose_message(void);

/** Display a "D" to show a draw. */
void draw_message(void);

/** Display an "R" to show a Rock move. */
void rock_message(void);

/** Display a "P" to show a Paper move. */
void paper_message(void);

/** Display an "S" to show a Snips move. */
void snips_message(void);

#endif