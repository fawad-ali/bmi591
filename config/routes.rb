Bmi591::Application.routes.draw do

  resources :authors
  resources :pub_med_articles
  resources :drugs

  get 'lab1' => "welcome#lab1", as: 'lab1'

  root 'welcome#index'

end
