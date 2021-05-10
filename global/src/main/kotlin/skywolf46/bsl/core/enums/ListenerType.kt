package skywolf46.bsl.core.enums

import skywolf46.bsl.core.abstraction.IListenerAttachable

sealed class ListenerType<TYPE : Any> {
    object SEND : ListenerType<IListenerAttachable>()
    object RECEIVE : ListenerType<IListenerAttachable>()
    object PROXY : ListenerType<IListenerAttachable>()
}