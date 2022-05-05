# Jetpack Compose Gestures

[![](https://jitpack.io/v/SmartToolFactory/Compose-Extended-Gestures.svg)](https://jitpack.io/#SmartToolFactory/Compose-Extended-Gestures)

Counterpart of `onTouchEvent` for Jetpack Compose and transform gesture with specific number of
pointers

## Modifier.pointerMotionEvents

Creates a modifier for processing pointer motion input within the region of the modified element.

After `AwaitPointerEventScope.awaitFirstDown` returns a `PointerInputChange` followed by `onDown` 
 at first pointer contact. Moving any pointer
invokes `AwaitPointerEventScope.awaitPointerEvent` then `onMove` is called. When last pointer is
up `onUp` is called. 

To prevent other pointer functions that call `awaitFirstDown`
or `awaitPointerEvent` (scroll, swipe, detect functions) 
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

Overload that returns list of pointers from `onMove`

```
fun Modifier.pointerMotionEventList(
    key1: Any? = Unit,
    onDown: (PointerInputChange) -> Unit = {},
    onMove: (List<PointerInputChange>) -> Unit = {},
    onUp: (PointerInputChange) -> Unit = {},
    delayAfterDownInMillis: Long = 0L
) 
```

* `delayAfterDownInMillis` parameter invokes Coroutines delay between `onDown`, and `onMove`.
  There is a delay about 20ms between in View's `onTouchEvent` first touch and move, similar delay might
  be required with Compose too, especially when drawing to `Canvas` which misses very fast events,
  Delaying move behavior might be required to detect whether is touch is in
  required region of Composable at first pointer contact.

`PointerInputChange` down and move events should be consumed if you need to prevent other gestures
like **scroll** or other **pointerInput**s to not intercept your gesture

```
       Modifier.pointerMotionEvents(
            onDown = {
                // When down is consumed
                it.consumeDownChange()
            },
            onMove = {
            // Consuming move prevents scroll other events to not get this move event
             it.consumePositionChange()
            },
            delayAfterDownInMillis =20
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
        numberOfPointersRequired = 2,
        onGesture = { gestureCentroid, gesturePan, gestureZoom, gestureRotate ->
            // Centroid, pan, zoom, and rotation only when 2 pointers are down
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
