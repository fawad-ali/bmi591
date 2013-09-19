class AtcCode < ActiveRecord::Base

  # attr_accessible :drug
  # attr_accessible :code

  def self.import_from_drugbank(codes,drug_id)
    items = []
    codes.each do |code|
      items << self.find_or_create_by_drug_id_and_code(drug_id, code.code)
    end
    items
  end
end
