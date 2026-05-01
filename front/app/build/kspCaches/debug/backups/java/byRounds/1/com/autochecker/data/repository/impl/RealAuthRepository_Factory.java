package com.autochecker.data.repository.impl;

import com.autochecker.core.network.ApiService;
import com.autochecker.data.local.TokenManager;
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
public final class RealAuthRepository_Factory implements Factory<RealAuthRepository> {
  private final Provider<ApiService> apiServiceProvider;

  private final Provider<TokenManager> tokenManagerProvider;

  public RealAuthRepository_Factory(Provider<ApiService> apiServiceProvider,
      Provider<TokenManager> tokenManagerProvider) {
    this.apiServiceProvider = apiServiceProvider;
    this.tokenManagerProvider = tokenManagerProvider;
  }

  @Override
  public RealAuthRepository get() {
    return newInstance(apiServiceProvider.get(), tokenManagerProvider.get());
  }

  public static RealAuthRepository_Factory create(Provider<ApiService> apiServiceProvider,
      Provider<TokenManager> tokenManagerProvider) {
    return new RealAuthRepository_Factory(apiServiceProvider, tokenManagerProvider);
  }

  public static RealAuthRepository newInstance(ApiService apiService, TokenManager tokenManager) {
    return new RealAuthRepository(apiService, tokenManager);
  }
}
