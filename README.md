# Jetpack Compose Gestures

![jitpack](https://jitpack.io/v/SmartToolFactory/Compose-Extended-Gestures.svg)

Counterpart of `onTouchEvent` for Jetpack Compose and transform gesture with specific number of
pointers

## Modifier.pointerMotionEvents

Creates a modifier for processing pointer motion input within the region of the modified element.

After `AwaitPointerEventScope.awaitFirstDown` returned a `PointerInputChange` then `onDown` is
called at first pointer contact. Moving any pointer
causes `AwaitPointerEventScope.awaitPointerEvent` then `onMove` is called. When last pointer is
up `onUp` is called. To prevent other pointer functions that call `awaitFirstDown`
or `awaitPointerEvent` (scroll, swipe, detect functions) receiving changes
call `PointerInputChange.consumeDownChange` in `onDown`, and
call `PointerInputChange.consumePositionChange` in `onMove` block.

```
fun Modifier.pointerMotionEvents(
    vararg keys: Any?,
    onDown: (PointerInputChange) -> Unit = {},
    onMove: (PointerInputChange) -> Unit = {},
    onUp: (PointerInputChange) -> Unit = {},
    delayAfterDownInMillis: Long = 0L
) = this.then(
    Modifier.pointerInput(keys) {
        detectMotionEvents(onDown, onMove, onUp, delayAfterDownInMillis)
    }
)
```

and the one returns list of pointer on move

```
fun Modifier.pointerMotionEventList(
    key1: Any? = Unit,
    onDown: (PointerInputChange) -> Unit = {},
    onMove: (List<PointerInputChange>) -> Unit = {},
    onUp: (PointerInputChange) -> Unit = {},
    delayAfterDownInMillis: Long = 0L
) 
```

* `delayAfterDownInMillis` parameter invokes Coroutines delay between first `down`, and `onMove`.
  There is a delay about 20ms between in View's `onTouchEvent` first touch and move and this might
  be required with Compose too, especially when drawing to `Canvas` which misses very fast events,
  this might cause missing first touch that might be required to detect whether is touch is in
  required region of Composable.

`PointerInputChange` down and move events should be consumed if you need to prevent other gestures
like **scroll** or other **pointerInput**s to not intercept your gesture

```
        val dragModifier = Modifier.pointerMotionEvents(
            onDown = {
                // When down is consumed
                it.consumeDownChange()
            },
            onMove = {
            // Consuming move prevents scroll other events to not get this move event
             it.consumePositionChange()
            },

        )
```

You can refer [this answer](https://stackoverflow.com/a/70847531/5457853) for details.

## Modifier.detectMultiplePointerTransformGestures

Returns the rotation, in degrees, of the pointers between the `PointerInputChange.previousPosition`
and `PointerInputChange.position` states. Only number of pointers that equal
to `numberOfPointersRequired` that are down in both previous and current states are considered.

Usage

```
Modifier.pointerInput(Unit) {
    detectMultiplePointerTransformGestures(
        onGesture = { gestureCentroid, gesturePan, gestureZoom, gestureRotate ->
            val oldScale = zoom
            val newScale = zoom * gestureZoom

            // For natural zooming and rotating, the centroid of the gesture should
            // be the fixed point where zooming and rotating occurs.
            // We compute where the centroid was (in the pre-transformed coordinate
            // space), and then compute where it will be after this delta.
            // We then compute what the new offset should be to keep the centroid
            // visually stationary for rotating and zooming, and also apply the pan.
            offset = (offset + gestureCentroid / oldScale).rotateBy(gestureRotate) -
                    (gestureCentroid / newScale + gesturePan / oldScale)
            zoom = newScale.coerceIn(0.5f..5f)
            angle += gestureRotate

            transformDetailText =
                "Zoom: ${decimalFormat.format(zoom)}, centroid: $gestureCentroid\n" +
                        "angle: ${decimalFormat.format(angle)}, " +
                        "Rotate: ${decimalFormat.format(gestureRotate)}, pan: $gesturePan"
        }
    )
}
```

## Gradle Setup

To get a Git project into your build:

* Step 1. Add the JitPack repository to your build file Add it in your root build.gradle at the end
  of repositories:
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

* Step 2. Add the dependency

```
dependencies {
        implementation 'com.github.SmartToolFactory:Compose-Extended-Gestures:Tag'
}
```