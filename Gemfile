source 'https://rubygems.org'
ruby '2.0.0'

gem 'rails', '4.0.0'


# Twitter bootstrap layout.
# gem "therubyracer"
# gem "less-rails"
gem 'twitter-bootstrap-rails'


gem	'drugbank'


gem 'slim-rails'	# Templating


gem 'jquery-rails'
gem 'jquery-ui-rails'

# gem 'jquery-datetimepicker'	# Better date/time selection.

gem 'sass-rails',	'~> 4.0.0'
gem 'coffee-rails',	'~> 4.0.0'
gem 'uglifier',		'>= 1.3.0'
gem 'turbolinks'


group :doc do
  # bundle exec rake doc:rails generates the API under doc/api.
  gem 'sdoc', require: false
end


group :development, :test do
	gem 'railroady'		# Diagraming
	gem 'sqlite3'
	gem 'capistrano'	# Deploy with Capistrano
	gem 'rvm-capistrano'

	# Better debugging
	gem 'binding_of_caller'
	gem 'better_errors'
end

group :production do
	gem 'mysql'
end
