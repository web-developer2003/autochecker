package com.autochecker.data.repository.impl;

import com.autochecker.core.network.ApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class RealUserRepository_Factory implements Factory<RealUserRepository> {
  private final Provider<ApiService> apiServiceProvider;

  public RealUserRepository_Factory(Provider<ApiService> apiServiceProvider) {
    this.apiServiceProvider = apiServiceProvider;
  }

  @Override
  public RealUserRepository get() {
    return newInstance(apiServiceProvider.get());
  }

  public static RealUserRepository_Factory create(Provider<ApiService> apiServiceProvider) {
    return new RealUserRepository_Factory(apiServiceProvider);
  }

  public static RealUserRepository newInstance(ApiService apiService) {
    return new RealUserRepository(apiService);
  }
}
