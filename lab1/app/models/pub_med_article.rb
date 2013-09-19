class PubMedArticle < ActiveRecord::Base

	searchable do
		integer	:pm_id
		text	:title,		stored: true #,	termVectors: true,	termPositions: true,	termOffsets: true
		text	:abstract,	stored: true #,	termVectors: true,	termPositions: true,	termOffsets: true
		text	:authors do
			authors.map { |a| "#{a.given_name} #{a.surname}"}
		end
		string	:journal_name,	stored: true
		date	:publication_date
	end

	has_many	:authors, dependent: :destroy

end
