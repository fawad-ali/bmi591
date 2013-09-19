class ExternalLink < ActiveRecord::Base

  # attr_accessible :drug, :name, :url

  def self.import_from_drugbank(external_links,drug_id)
    items = []
    external_links.each do |external_link|
      item = self.find_or_create_by_drug_id_and_url(drug_id, external_link.url)
      item.update_attributes(external_link.attributes)
      items << item
    end
    items
  end
end
