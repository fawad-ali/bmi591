class Packager < ActiveRecord::Base

  # attr_accessible :name, :url

  def self.import_from_drugbank(packagers, drug_id)
    items = []
    packagers.each do |packager|
      item = self.find_or_create_by_name_and_url(packager.name, packager.url)
      items << item
    end
    items
  end
end
