class DrugGroup < ActiveRecord::Base

  # attr_accessible :name  

  def self.import_from_drugbank(groups, drug_id)
    items = []
    groups.each do |group|
      items << self.find_or_create_by_name(group.name)
    end
    items
  end
end
