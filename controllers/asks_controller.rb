class AsksController < ApplicationController
  def new
    @ask = Ask.new
  end

  def create
    require "net/http"
    require "uri"
    require "json"

    @ask = Ask.new(ask_params)
    link_to_cloudsearch = "http://search-xiaolongjiang-twittermap-1-pjbksjqnrpp52x3bqsglij5ofe.us-east-1.cloudsearch.amazonaws.com/2013-01-01/search?q=" + @ask.query + "&return=_all_fields"
    uri = URI.parse(link_to_cloudsearch)

    # Get Response of HTTP Request
    @response_cloud_search = Net::HTTP.get_response(uri)

    # Parse Request into JSON file.
    @parsed = JSON.parse @response_cloud_search.body

    if @ask.save
      render 'cloud_search/request_to_cloudsearch'
      # redirect_to :back
    end
  end

  private

  def ask_params
    params.require(:ask).permit(:query)
  end
end
