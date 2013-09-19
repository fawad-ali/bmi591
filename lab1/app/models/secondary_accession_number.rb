class SecondaryAccessionNumber < ActiveRecord::Base

  # attr_accessible :drug_id, :number

  def self.import_from_drugbank(numbers,drug_id)
    items = []
    numbers.each do |number|
      items << self.find_or_create_by_drug_id_and_number(drug_id, number.number)
    end
    items
  end
end
