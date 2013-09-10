class Author < ActiveRecord::Base

	belongs_to	:pub_med_article

	validates_presence_of	:pub_med_article

end
