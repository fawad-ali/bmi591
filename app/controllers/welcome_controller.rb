class WelcomeController < ApplicationController

	def index
		
	end

	def lab1
		@s1 = PubMedArticle.search do
			fulltext 'mouse' do
				fields(:title, :abstract)
			end
			with(:journal_name, 'Accounts of Chemical Research')
		end

		@s2 = PubMedArticle.search do
			fulltext 'cyclooxygenase' do
				fields(:title, :abstract)
			end
			with(:journal_name, 'The AAPS Journal')
		end

		@s3 = PubMedArticle.search do
			with(:journal_name, 'ACS Chemical Biology')
		end
		
		@s4 = PubMedArticle.search do
			with(:journal_name, 'Acta Histochemica et Cytochemica')
		end
		
		@s5 = PubMedArticle.search do
			with(:journal_name, 'ACS Medicinal Chemistry Letters')
		end
		
	end



end
