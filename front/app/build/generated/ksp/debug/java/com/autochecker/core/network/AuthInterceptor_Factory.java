package com.autochecker.core.network;

import com.autochecker.data.local.DataStoreManager;
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
public final class AuthInterceptor_Factory implements Factory<AuthInterceptor> {
  private final Provider<TokenManager> tokenManagerProvider;

  private final Provider<DataStoreManager> dataStoreManagerProvider;

  public AuthInterceptor_Factory(Provider<TokenManager> tokenManagerProvider,
      Provider<DataStoreManager> dataStoreManagerProvider) {
    this.tokenManagerProvider = tokenManagerProvider;
    this.dataStoreManagerProvider = dataStoreManagerProvider;
  }

  @Override
  public AuthInterceptor get() {
    return newInstance(tokenManagerProvider.get(), dataStoreManagerProvider.get());
  }

  public static AuthInterceptor_Factory create(Provider<TokenManager> tokenManagerProvider,
      Provider<DataStoreManager> dataStoreManagerProvider) {
    return new AuthInterceptor_Factory(tokenManagerProvider, dataStoreManagerProvider);
  }

  public static AuthInterceptor newInstance(TokenManager tokenManager,
      DataStoreManager dataStoreManager) {
    return new AuthInterceptor(tokenManager, dataStoreManager);
  }
}
