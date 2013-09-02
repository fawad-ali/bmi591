class Brand < ActiveRecord::Base

  # attr_accessible :name

  def self.import_from_drugbank(brands,drug_id)
    items = []
    brands.each do |brand|
      items << self.find_or_create_by_drug_id_and_name(drug_id, brand.name)
    end
    items
  end
end
