package com.hhu.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface TBOrderMapper {
    @Insert("insert into tb_order (id) values (#{id})")
    void save(String id);

    @Select("select order_state from tb_order where id = #{id} for update")
    int findOrderStateByIdForUpdate(String id);

    @Update("update tb_order set order_state = #{orderstate} where id = #{id}")
    void updateOrderState(int orderstate,String id);
}
