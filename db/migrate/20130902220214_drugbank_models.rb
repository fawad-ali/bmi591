class DrugbankModels < ActiveRecord::Migration

	def change

		create_table "atc_codes", force: true do |t|
			t.integer  "drug_id"
			t.string   "code"
			t.datetime "created_at"
			t.datetime "updated_at"
		end
	
		add_index "atc_codes", ["drug_id"], name: "atc_codes_drug_id_idx"

		create_table "brands", force: true do |t|
			t.string   "name"
			t.integer  "drug_id"
			t.datetime "created_at"
			t.datetime "updated_at"
		end
	
		add_index "brands", ["name"], name: "brands_names_idx"

	
		create_table "dosages", force: true do |t|
			t.integer  "drug_id"
			t.string   "form"
			t.string   "route"
			t.string   "strength"
			t.datetime "created_at"
			t.datetime "updated_at"
		end
	
		add_index "dosages", ["drug_id"], name: "dosages_drug_id_idx"
	
		create_table "drug_categories", force: true do |t|
			t.string   "name"
			t.datetime "created_at"
			t.datetime "updated_at"
		end
	
		create_table "drug_groups", force: true do |t|
			t.string   "name"
			t.datetime "created_at"
			t.datetime "updated_at"
		end
	
		create_table "drug_interactions", force: true do |t|
			t.integer  "drug_id"
			t.string   "interaction_drug_id"
			t.text     "description"
			t.datetime "created_at"
			t.datetime "updated_at"
			t.string   "name"
		end
	
		add_index "drug_interactions", ["drug_id"], name: "drug_interactions_drug_id_idx"
	
		create_table "drugs", force: true do |t|
			t.string   "name"
			t.string   "description"
			t.string   "url"
			t.string   "chemical_formula"
			t.string   "absorption"
			t.string   "drug_type"
			t.string   "chemical_iupac_name"
			t.string   "cas_registry_number"
			t.string   "pharmacology"
			t.string   "mechanism_of_action"
			t.string   "indication"
			t.datetime "created_at"
			t.datetime "updated_at"
			t.string   "drugbank_id"
			t.string   "toxicity"
			t.string   "biotransformation"
			t.string   "half_life"
			t.string   "protein_binding"
			t.string   "route_of_elimination"
			t.string   "volume_of_distribution"
			t.string   "clearance"
			t.text     "general_references"
			t.text     "synthesis_reference"
		end
	
		add_index "drugs", ["drugbank_id"], name: "drugbank_id_idx"
		add_index "drugs", ["name"], name: "drugs_names_idx"
		add_index "drugs", ["name"], name: "index_drugs_on_name", unique: true
	
		create_table "drugs_drug_categories", id: false, force: true do |t|
			t.integer "drug_id"
			t.integer "drug_category_id"
		end
	
		add_index "drugs_drug_categories", ["drug_id", "drug_category_id"], name: "drugs_drug_categories_idx", unique: true
	
		create_table "drugs_drug_groups", id: false, force: true do |t|
			t.integer "drug_id"
			t.integer "drug_group_id"
		end
	
		add_index "drugs_drug_groups", ["drug_id", "drug_group_id"], name: "drugs_drug_groups_idx", unique: true
	
		create_table "drugs_manufacturers", id: false, force: true do |t|
			t.integer "drug_id"
			t.integer "manufacturer_id"
		end
	
		add_index "drugs_manufacturers", ["drug_id", "manufacturer_id"], name: "drugs_manufacturers_idx", unique: true
	
		create_table "drugs_packagers", id: false, force: true do |t|
			t.integer "drug_id"
			t.integer "packager_id"
		end
	
		add_index "drugs_packagers", ["drug_id", "packager_id"], name: "drugs_packagers_idx", unique: true
	
		create_table "external_ids", force: true do |t|
			t.integer  "drug_id"
			t.string   "name"
			t.string   "external_id"
			t.datetime "created_at"
			t.datetime "updated_at"
		end
	
		add_index "external_ids", ["drug_id"], name: "external_ids_drug_id_idx"
	
		create_table "external_links", force: true do |t|
			t.integer  "drug_id"
			t.string   "name"
			t.string   "url"
			t.datetime "created_at"
			t.datetime "updated_at"
		end
	
		add_index "external_links", ["drug_id"], name: "external_links_drug_id_idx"
	
		create_table "manufacturers", force: true do |t|
			t.string   "name"
			t.string   "url"
			t.boolean  "generic",    default: false
			t.datetime "created_at"
			t.datetime "updated_at"
		end
	
		create_table "packagers", force: true do |t|
			t.string   "name"
			t.string   "url"
			t.datetime "created_at"
			t.datetime "updated_at"
		end
	
		create_table "patents", force: true do |t|
			t.integer  "drug_id"
			t.string   "number"
			t.string   "country"
			t.date     "approved_at"
			t.date     "expires_at"
			t.datetime "created_at"
			t.datetime "updated_at"
		end
	
		add_index "patents", ["drug_id"], name: "patents_drug_id_idx"
	
		create_table "prices", force: true do |t|
			t.integer  "drug_id"
			t.string   "description"
			t.decimal  "cost"
			t.string   "currency"
			t.string   "unit"
			t.datetime "created_at"
			t.datetime "updated_at"
		end
	
		add_index "prices", ["drug_id"], name: "prices_drug_id_idx"
	
	
		create_table "secondary_accession_numbers", force: true do |t|
			t.integer  "drug_id"
			t.string   "number"
			t.datetime "created_at"
			t.datetime "updated_at"
		end
	
		add_index "secondary_accession_numbers", ["drug_id"], name: "secondary_accession_numbers_drug_id_idx"
	
		create_table "synonyms", force: true do |t|
			t.integer  "drug_id"
			t.string   "name"
			t.datetime "created_at"
			t.datetime "updated_at"
		end
	
		add_index "synonyms", ["drug_id"], name: "synonyms_drug_id_idx"

	
	end

end
