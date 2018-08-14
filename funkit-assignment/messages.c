/**
  File: messages.c
  Authors: Justin Thorby, Matt Hodgett
  Date: 12 Oct 2016
  Description: Display a variety of messages/characters for a Paper-Snips-Rock
               game.
*/

#include "system.h"
#include "tinygl.h"
#include "../fonts/font5x7_1.h"


/** Display a welcome message. */
void welcome_message(void)
{
  tinygl_text_mode_set(TINYGL_TEXT_MODE_SCROLL);
  tinygl_text("Up-Rock Right-Paper Left-Snips");
}

/** Clear the screen to hide player's move. */
void clear_screen(void)
{
  tinygl_clear();
  tinygl_update();
}

/** Display a "W" to show a win. */
void win_message(void)
{
  tinygl_text_mode_set(TINYGL_TEXT_MODE_SCROLL);
  tinygl_text("WINNER");
}

/** Display an "L" to show a loss. */
void lose_message(void)
{
  tinygl_text_mode_set(TINYGL_TEXT_MODE_SCROLL);
  tinygl_text("DISAPPOINTING");
}

/** Display a "D" to show a draw. */
void draw_message(void)
{
  tinygl_text_mode_set(TINYGL_TEXT_MODE_SCROLL);
  tinygl_text("BORING");
}

/** Display an "R" to show a Rock move. */
void rock_message(void)
{
  tinygl_text_mode_set(TINYGL_TEXT_MODE_STEP);
  tinygl_text("R");
}

/** Display a "P" to show a Paper move. */
void paper_message(void)
{
  tinygl_text_mode_set(TINYGL_TEXT_MODE_STEP);
  tinygl_text("P");
}

/** Display an "S" to show a Snips move. */
void snips_message(void)
{
  tinygl_text_mode_set(TINYGL_TEXT_MODE_STEP);
  tinygl_text("S");
}