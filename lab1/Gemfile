source 'https://rubygems.org'
ruby '2.0.0'

gem 'rails', '4.0.0'


# Twitter bootstrap layout.
# gem "therubyracer"
# gem "less-rails"
# gem 'twitter-bootstrap-rails'	# Old Bootstrap 2
gem 'twitter-bootstrap-rails', git: 'git://github.com/seyhunak/twitter-bootstrap-rails.git', branch: 'bootstrap3'



gem 'friendly_id',	git: 'git://github.com/FriendlyId/friendly_id'	# Better paths.


gem	'drugbank'


gem 'slim-rails'	# Templating

gem 'inherited_resources'

gem 'jquery-rails'
gem 'jquery-ui-rails'

gem	'will_paginate'
# gem 'jquery-datetimepicker'	# Better date/time selection.

gem 'sass-rails',	'~> 4.0.0'
gem 'coffee-rails',	'~> 4.0.0'
gem 'uglifier',		'>= 1.3.0'


# Solr stuff
gem 'sunspot_rails'
gem 'sunspot_solr' # optional pre-packaged Solr distribution for use in development
gem 'progress_bar'

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
