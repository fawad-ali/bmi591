class ExternalId < ActiveRecord::Base

  # attr_accessible :drug_id, :external_id, :name

  def self.import_from_drugbank(external_ids,drug_id)
    items = []
    external_ids.each do |external_id|
      item = self.find_or_create_by_drug_id_and_external_id(drug_id, external_id.id)
      item.update_attributes(external_id.attributes)
      items << item
    end
    items
  end
end
