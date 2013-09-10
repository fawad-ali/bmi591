require 'test_helper'

class PubMedArticlesControllerTest < ActionController::TestCase
  setup do
    @pub_med_article = pub_med_articles(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:pub_med_articles)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create pub_med_article" do
    assert_difference('PubMedArticle.count') do
      post :create, pub_med_article: { abstract: @pub_med_article.abstract, authors: @pub_med_article.authors, journal_name: @pub_med_article.journal_name, pm_id: @pub_med_article.pm_id, publication_date: @pub_med_article.publication_date, title: @pub_med_article.title }
    end

    assert_redirected_to pub_med_article_path(assigns(:pub_med_article))
  end

  test "should show pub_med_article" do
    get :show, id: @pub_med_article
    assert_response :success
  end

  test "should get edit" do
    get :edit, id: @pub_med_article
    assert_response :success
  end

  test "should update pub_med_article" do
    patch :update, id: @pub_med_article, pub_med_article: { abstract: @pub_med_article.abstract, authors: @pub_med_article.authors, journal_name: @pub_med_article.journal_name, pm_id: @pub_med_article.pm_id, publication_date: @pub_med_article.publication_date, title: @pub_med_article.title }
    assert_redirected_to pub_med_article_path(assigns(:pub_med_article))
  end

  test "should destroy pub_med_article" do
    assert_difference('PubMedArticle.count', -1) do
      delete :destroy, id: @pub_med_article
    end

    assert_redirected_to pub_med_articles_path
  end
end
