package parsley.debugger.frontend.internal

import parsley.debugger.{DebugTree, ParseAttempt}
import scalafx.beans.binding.Bindings
import scalafx.beans.property.ObjectProperty
import scalafx.geometry.Pos
import scalafx.scene.control.ScrollPane
import scalafx.scene.control.ScrollPane.ScrollBarPolicy
import scalafx.scene.layout.{GridPane, Priority, VBox}
import scalafx.scene.text.{FontWeight, Text, TextFlow}

private[frontend] class AttemptInfo(dtree: ObjectProperty[Option[DebugTree]]) extends ScrollPane {
  // Makes sure the content doesn't go off the sides:
  fitToWidth = true
  hgrow = Priority.Always

  background = DefaultBackground

  hbarPolicy = ScrollBarPolicy.Never
  vbarPolicy = ScrollBarPolicy.Never

  // Contents.
  // Finally set content to the list of attempts.
  content <== Bindings.createObjectBinding(
    () => {
      // We also want to reset the scrollbar to the top, too.
      vvalue = 0

      val allList = new VBox()

      if (dtree().isDefined) {
        for (att <- dtree().get.parseResults.map(new Attempt(_))) {
          att.prefHeight <== height
          allList.children.add(att)
        }
      }

      allList.delegate
    },
    dtree
  )
}

private[frontend] class Attempt(att: ParseAttempt) extends GridPane {
  // Visual parameters.
  background = simpleBackground(if (att.success) SuccessColour else FailureColour)

  hgap = relativeSize(1)
  vgap = relativeSize(0.5)

  padding = simpleInsets(1)

//  prefWidth <== outer.width

  hgrow = Priority.Always

  alignment = Pos.CenterLeft

  // Contents.
  add(
    {
      val text = new Text {
        text = if (att.fromOffset == att.toOffset) {
          "*** Parser did not consume input. ***"
        } else {
          val untilLB  = att.rawInput.takeWhile(!"\r\n".contains(_))
          val addition = if (att.rawInput.length > untilLB.length) " [...]" else ""

          s"\"${untilLB + addition}\""
        }
        font = monoFont(1, FontWeight.Bold)
      }
      new TextFlow(text)
    },
    1,
    0
  )

  add(
    {
      val text = new Text {
        text =
          if (att.fromOffset == att.toOffset) "N/A"
          else s"${att.fromPos} to ${att.toPos}"
        font = defaultFont(1)
      }
      new TextFlow(text)
    },
    1,
    1
  )

  add(
    {
      val resultText = new Text { text = "Result: "; font = defaultFont(1, FontWeight.Bold) }
      val itemText   = new Text { text = att.result.mkString; font = monoFont(1) }
      new TextFlow(resultText, itemText)
    },
    columnIndex = 1,
    rowIndex = 2
  )

  add(
    new Text {
      text = if (att.success) "✓" else "✗"
      font = defaultFont(3, FontWeight.Black)
    },
    0,
    0,
    1,
    3
  )
}
