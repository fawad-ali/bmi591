class Manufacturer < ActiveRecord::Base

  # attr_accessible :generic, :name, :url

  def self.import_from_drugbank(manufacturers, drug_id)
    items = []
    manufacturers.each do |manufacturer|
      item = self.find_or_create_by_name_and_url(manufacturer.name, manufacturer.url)
      item.update_attributes(manufacturer.attributes)
      items << item
    end
    items
  end
end
