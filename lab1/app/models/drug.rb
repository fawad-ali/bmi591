class Drug < ActiveRecord::Base

	extend FriendlyId
	friendly_id :name, use: :slugged

	validates_presence_of :name
	validates_uniqueness_of :name

	has_many :atc_codes,	dependent: :destroy
	has_many :brands,	dependent: :destroy
	has_many :dosages,	dependent: :destroy
	has_many :drug_interactions,	dependent: :destroy
	has_many :external_ids,	dependent: :destroy
	has_many :external_links,	dependent: :destroy
	has_many :patents,	dependent: :destroy
	has_many :prices,	dependent: :destroy
	has_many :rules,	dependent: :destroy
	has_many :secondary_accession_numbers,	dependent: :destroy
	has_many :synonyms,	dependent: :destroy

	has_and_belongs_to_many :drug_categories, :join_table => 'drugs_drug_categories'
	has_and_belongs_to_many :drug_groups, :join_table => 'drugs_drug_groups'
	has_and_belongs_to_many :packagers, :join_table => 'drugs_packagers'
	has_and_belongs_to_many :manufacturers, :join_table => 'drugs_manufacturers'


	DRUGBANK_MAPPINGS = {'id' => 'drugbank_id', 'type' => 'drug_type'}
	DRUGBANK_RELATIONSHIP_MAPPINGS = {'groups' => 'drug_groups', 'categories' => 'drug_categories'}

	scope :named_like, lambda{|q| includes(:brands).where(["drugs.name like ? OR brands.name like ?", "#{q}%", "#{q}%"])}

	def as_json(options={})
		super.merge({:display => self.to_s})
	end

	def to_s
		brands.empty? ? name : "#{name} (a.k.a. #{self.brands.collect(&:name).join(', ')})"
	end

	def self.import_from_drugbank(drugs)
		drugs.each do |drug|
			begin
				new_drug = ::Drug.find_or_create_by_drugbank_id(drug.id)
				new_drug.update_attributes(Hash[drug.attributes.map{|k, v| [DRUGBANK_MAPPINGS[k]||k, v] }])
				drug.relations.each_pair do |relationship, models|
					relationship = DRUGBANK_RELATIONSHIP_MAPPINGS[relationship] || relationship
					related_items = relationship.classify.constantize.import_from_drugbank(models, new_drug.id)
					new_drug.send(relationship + '=', related_items) 
				end
			rescue Exception => e
				puts e.message
			end
		end
	end

end