class DrugCategory < ActiveRecord::Base

  # attr_accessible :name

  def self.import_from_drugbank(categories, drug_id)
    items = []
    categories.each do |category|
      items << self.find_or_create_by_name(category.name)
    end
    items
  end
end
