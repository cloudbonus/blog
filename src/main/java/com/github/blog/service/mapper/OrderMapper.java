package com.github.blog.service.mapper;

import com.github.blog.controller.dto.common.OrderDto;
import com.github.blog.controller.dto.request.OrderRequest;
import com.github.blog.controller.dto.request.filter.OrderFilterRequest;
import com.github.blog.model.Order;
import com.github.blog.repository.dto.filter.OrderFilter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {PostMapper.class, UserMapper.class})
public interface OrderMapper extends BasePageMapper<Order, OrderDto> {
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "postId", target = "post.id")
    Order toEntity(OrderRequest request);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "postId", source = "post.id")
    OrderDto toDto(Order order);

    OrderFilter toEntity(OrderFilterRequest requestFilter);
}