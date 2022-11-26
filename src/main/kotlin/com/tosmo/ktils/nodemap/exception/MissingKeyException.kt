package com.tosmo.ktils.nodemap.exception

import com.tosmo.ktils.nodemap.data.Key


/**
 *
 * @author Thomas Miao
 */
class MissingKeyException(message: String? = null) : Exception("找不到键${message?.let { ":${message}" } ?: ""}") {

    constructor(key: Key) : this("[$key.keyName]")
}