class AddSlugToDrugs < ActiveRecord::Migration

	def change
		add_column :drugs, :slug, :string
	end

end
