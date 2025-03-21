package com.henni.handwriting.model

/**
 * ToolMode represents the different modes available for user interaction.
 * It includes modes for drawing, erasing, selecting, and moving content.
 */

enum class ToolMode {
  /**
   * Allow users to freely draw or write.
   */
  PenMode,

  /**
   * Allow users to erase existing lines or content.
   */
  EraserMode,

  /**
   * Allow users to select a specific area.
   */
  LassoSelectMode,

  /**
   * Allow users to move the selected area.
   */
  LassoMoveMode,

  /**
   * Allow users to draw or interact with laser-like lines for precise pointing or highlighting.
   */
  LineLaserMode,
}
