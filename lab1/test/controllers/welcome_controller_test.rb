require 'test_helper'

class WelcomeControllerTest < ActionController::TestCase
  test "should get lab1" do
    get :lab1
    assert_response :success
  end

end
