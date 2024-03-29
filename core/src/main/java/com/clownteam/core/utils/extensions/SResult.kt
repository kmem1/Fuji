package com.clownteam.core.utils.extensions

import com.clownteam.core.domain.SResult

/**
 *  Преобразует строку в тост
 */
fun String.toToastResult() = SResult.Toast(this)

/**
 *  IdRes в тост
 */
fun Int.toToastResult() = SResult.Toast(this)

/**
 * Any в тост
 */
fun Any.toToastResult() = SResult.Toast(this)

/**
 *  Любая переменная в SResult
 */
fun <T : Any> T.toSuccessResult() = SResult.Success(this)

/**
 *  Success с необходимостью использовать
 */
fun successHandlingResult() = SResult.Success(Unit).apply {
    isNeedHandle = true
}

/**
 *  Строка в неудачный результат
 */
fun String?.toFailedResult() = SResult.Failed(this, null)

/**
 *  Int в неудачный результат
 */
fun Int.toFailedResult() = SResult.Failed(this, null)

/**
 *  Любая переменная в неудачный результат
 */
fun <T : Any?> T?.toFailedResult() = SResult.Failed(this, null)

/**
 *  Функция для создания "загрузки"
 */
fun loadingResult() = SResult.Loading(null)

/**
 *  Загрузка с данными
 */
fun <T : Any?> T?.toLoadingResult() = SResult.Loading(this)

/**
 *  Функция для создания завершенного результата
 */
fun completedResult() = SResult.Completed

/**
 * Функция для создания навигации назад
 */
fun navBackResult() = SResult.NavBackResult

/**
 *  Failed без результатов
 */
fun emptyFailed() = SResult.Failed(null, null)

inline fun <T : Any> SResult<T>.dataOrFailed(onFailed: (message: Any?) -> T): T =
    data ?: onFailed(if (this is SResult.Failed) message else null)


