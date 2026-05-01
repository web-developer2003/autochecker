from allauth.account.forms import SignupForm
from allauth.socialaccount.forms import SignupForm as SocialSignupForm


class UserSignupForm(SignupForm):
    pass


class UserSocialSignupForm(SocialSignupForm):
    pass
