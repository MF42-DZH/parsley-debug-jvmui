package parsley.debugger.frontend.internal

import scalafx.geometry.Orientation
import scalafx.scene.{Node, Scene}
import scalafx.scene.control.SplitPane

// Three-way split pane. Allows a right and left hand main split.
private[frontend] class ThreeSplitPane(
  outer: Scene,
  mainChild: Node,
  topChild: Node,
  bottomChild: Node,
  leftyFlip: Boolean = false,
  defaultPositions: (Double, Double) = (0.75, 0.25)
) extends SplitPane {
  // Set our baseline capabilities.
  prefWidth  <== outer.width
  prefHeight <== outer.height

  // Make our vertical pane.
  private val innerPane = new SplitPane {
    orientation = Orientation.Vertical
  }

  // Place the children in their expected positions.
  innerPane.items.addAll(topChild, bottomChild)

  // Now: if we want a left flip, put the inner pane on the left.
  if (leftyFlip) items.addAll(innerPane, mainChild)
  else items.addAll(mainChild, innerPane)

  // Keep width of sidebar constant when resizing window.
  outer.width.addListener { (_, ov, nv) =>
    // Get pixel size of old RHS and then apply it to the new one.
    val currentDiv = (1 - dividers.head.getPosition) * ov.doubleValue()
    val newDiv     = 1 - (currentDiv / nv.doubleValue())

    dividers.collectFirst(_.setPosition(newDiv))
  }

  outer.height.addListener { (_, ov, nv) =>
    // Get pixel size of old top and then apply it to the new one.
    val currentDiv = innerPane.dividers.head.getPosition * ov.doubleValue()
    val newDiv     = currentDiv / nv.doubleValue()

    innerPane.dividers.collectFirst(_.setPosition(newDiv))
  }

  // Set the initial starting locations of the splits.
  dividers.collectFirst(_.setPosition(defaultPositions._1))
  innerPane.dividers.collectFirst(_.setPosition(defaultPositions._2))

  // Other miscellaneous properties.
  background = DefaultBackground
}
