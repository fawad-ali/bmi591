class Synonym < ActiveRecord::Base

  # attr_accessible :drug_id, :name

  def self.import_from_drugbank(synonyms, drug_id)
    items = []
    synonyms.each do |synonym|
      items << self.find_or_create_by_drug_id_and_name(drug_id, synonym.name)
    end
    items
  end
end
