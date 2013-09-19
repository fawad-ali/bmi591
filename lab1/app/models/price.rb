class Price < ActiveRecord::Base

  # attr_accessible :decimal, :references, :string, :string, :string  

  def self.import_from_drugbank(prices, drug_id)
    items = []
    prices.each do |price|
      item = self.find_or_create_by_drug_id_and_cost_and_currency_and_unit(drug_id, price.cost, price.currency, price.unit)
      item.update_attributes(price.attributes)
      items << item
    end
    items
  end
end
