package com.autochecker.data.mock;

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
public final class MockAuthRepository_Factory implements Factory<MockAuthRepository> {
  private final Provider<TokenManager> tokenManagerProvider;

  public MockAuthRepository_Factory(Provider<TokenManager> tokenManagerProvider) {
    this.tokenManagerProvider = tokenManagerProvider;
  }

  @Override
  public MockAuthRepository get() {
    return newInstance(tokenManagerProvider.get());
  }

  public static MockAuthRepository_Factory create(Provider<TokenManager> tokenManagerProvider) {
    return new MockAuthRepository_Factory(tokenManagerProvider);
  }

  public static MockAuthRepository newInstance(TokenManager tokenManager) {
    return new MockAuthRepository(tokenManager);
  }
}
