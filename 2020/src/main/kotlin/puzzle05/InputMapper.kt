package puzzle05

import InputMapper
import split

val mapper = InputMapper { content ->
    content.split()
        .map(Seat.Companion::from)
}