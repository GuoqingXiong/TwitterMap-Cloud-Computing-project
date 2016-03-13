class SearchController < ApplicationController
  def new
    @q = Search.new(:query => "hi")
  end
end
