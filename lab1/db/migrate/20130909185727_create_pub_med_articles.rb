class CreatePubMedArticles < ActiveRecord::Migration
  def change
    create_table :pub_med_articles do |t|
      t.integer :pm_id
      t.string :title
      t.string :authors
      t.text :abstract
      t.date :publication_date
      t.string :journal_name

      t.timestamps
    end
  end
end
