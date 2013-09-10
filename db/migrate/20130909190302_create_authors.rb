class CreateAuthors < ActiveRecord::Migration
  def change
    create_table :authors do |t|
      t.integer :pub_med_article_id
      t.string :given_name
      t.string :surname

      t.timestamps
    end
  end
end
