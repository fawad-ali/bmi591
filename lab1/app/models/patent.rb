class Patent < ActiveRecord::Base

  # attr_accessible :approved_at, :expires_at, :drug_id, :number, :country

  def self.import_from_drugbank(patents, drug_id)
    items = []
    patents.each do |patent|
      item = self.find_or_create_by_drug_id_patent_id_and_country(drug_id, patent.patent_id, patent.country)
      item.update_attribtues(item.attributes)
      items << item
    end
    items
  end
end
