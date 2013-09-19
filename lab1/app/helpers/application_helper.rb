module ApplicationHelper

	def yes_or_no(t)
		t ? 'Yes' : 'No'
	end

	def text_with_icon(text, icon)
		"<i class=\"icon icon-#{icon}\"></i> #{text}".html_safe
	end

	def icon(icon)
		"<i class=\"icon icon-#{icon}\"></i>".html_safe
	end

	def drugbank_url(id)
		"http://www.drugbank.ca/drugs/#{id}"
	end

	def cas_url(id)
		"http://www.ncbi.nlm.nih.gov/pccompound?term=#{id}"
	end

end
