class PubMedArticlesController < InheritedResources::Base

	def index
		if params['q']			
			# Solr search!
			@search = PubMedArticle.search do
				fulltext params['q'] do
					highlight	:title
					highlight	:abstract
				end
				paginate page: params[:page] || 1, per_page: 10
			end
			@pub_med_articles = @search.hits
		else
			@pub_med_articles = PubMedArticle.order(title: :asc).paginate(:page => params[:page])
		end

		respond_to do |format|
			format.html # index.html.erb
			format.json { render json: @pub_med_articles }
		end
	end
end
