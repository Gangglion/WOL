package com.glion.wol.di

import javax.inject.Qualifier

/**
 * Project : WOL
 * File : ApplicationScope
 * Created by glion on 2025-09-29
 *
 * Description:
 * - 앱 전역 Scope
 *
 * Copyright @2025 Gangglion. All rights reserved
 */

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScopeDefault

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScopeIO
