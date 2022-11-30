package com.tosmo.ktils.typemap.exception

import com.tosmo.ktils.typemap.data.TypeKey


/**
 *
 * @author Thomas Miao
 */
class MissingKeyException(message: String? = null) : Exception("找不到键${message?.let { ":${message}" } ?: ""}") {

    constructor(key: TypeKey) : this("[$key.keyName]")
}