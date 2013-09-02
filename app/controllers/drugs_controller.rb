class DrugsController < InheritedResources::Base

	before_action :set_friendly_resource, only: [:show]

	def index
		if params['q']
			@drugs = Drug.named_like(params['q']).order(name: :asc).paginate(:page => params[:page])
		else
			@drugs = Drug.order(name: :asc).paginate(:page => params[:page])
		end

		respond_to do |format|
			format.html # index.html.erb
			format.json { render json: @drugs }
		end
	end

	protected

    # Use callbacks to share common setup or constraints between actions.
    def set_friendly_resource
      @drug = Drug.friendly.find(params[:id])
    end

	def permitted_params
		params.permit(drug: [:drugbank_id, :name, :description, :cas_registry_number, :general_references, :synthesis_references, :indication, :pharmacology, :mechanism_of_action, :toxicity, :biotransformation, :absorption, :half_life, :protein_binding, :route_of_elimination, :volume_of_distribution, :clearance, :chemical_formula, :chemical_iupac_name, :drug_type, :brands, :tag_list, :url, :synthesis_reference])
	end

end
