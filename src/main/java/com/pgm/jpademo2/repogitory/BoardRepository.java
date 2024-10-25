package com.pgm.jpademo2.repogitory;

import com.pgm.jpademo2.domain.Board;
import com.pgm.jpademo2.repogitory.search.BoardSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

    @Query("select b from Board b where b.title like concat('%',:keyword,'%') order by b.bno desc")
    Page<Board> searchAll(String keyword, Pageable pageable);
}
